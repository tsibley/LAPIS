package org.genspectrum.lapis.auth

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.genspectrum.lapis.config.AccessKeys
import org.genspectrum.lapis.config.AccessKeysReader
import org.genspectrum.lapis.config.DatabaseConfig
import org.genspectrum.lapis.config.OpennessLevel
import org.genspectrum.lapis.controller.ACCESS_KEY_PROPERTY
import org.genspectrum.lapis.controller.AGGREGATED_ROUTE
import org.genspectrum.lapis.controller.AMINO_ACID_INSERTIONS_ROUTE
import org.genspectrum.lapis.controller.AMINO_ACID_MUTATIONS_ROUTE
import org.genspectrum.lapis.controller.LapisErrorResponse
import org.genspectrum.lapis.controller.NUCLEOTIDE_INSERTIONS_ROUTE
import org.genspectrum.lapis.controller.NUCLEOTIDE_MUTATIONS_ROUTE
import org.genspectrum.lapis.util.CachedBodyHttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ProblemDetail
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class DataOpennessAuthorizationFilterFactory(
    private val databaseConfig: DatabaseConfig,
    private val objectMapper: ObjectMapper,
    private val accessKeysReader: AccessKeysReader,
) {
    fun create() =
        when (databaseConfig.schema.opennessLevel) {
            OpennessLevel.OPEN -> AlwaysAuthorizedAuthorizationFilter(objectMapper)
            OpennessLevel.PROTECTED -> ProtectedDataAuthorizationFilter(
                objectMapper,
                accessKeysReader.read(),
                databaseConfig.schema.metadata.filter { it.valuesAreUnique }.map { it.name },
            )
        }
}

abstract class DataOpennessAuthorizationFilter(protected val objectMapper: ObjectMapper) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val reReadableRequest = makeRequestBodyReadableMoreThanOnce(request)

        when (val result = isAuthorizedForEndpoint(reReadableRequest)) {
            AuthorizationResult.Success -> filterChain.doFilter(reReadableRequest, response)
            is AuthorizationResult.Failure -> {
                response.status = HttpStatus.FORBIDDEN.value()
                response.contentType = MediaType.APPLICATION_JSON_VALUE
                response.writer.write(
                    objectMapper.writeValueAsString(
                        LapisErrorResponse(
                            ProblemDetail.forStatus(HttpStatus.FORBIDDEN).also {
                                it.title = HttpStatus.FORBIDDEN.reasonPhrase
                                it.detail = result.message
                            },
                        ),
                    ),
                )
            }
        }
    }

    private fun makeRequestBodyReadableMoreThanOnce(request: HttpServletRequest) =
        CachedBodyHttpServletRequest(request, objectMapper)

    abstract fun isAuthorizedForEndpoint(request: CachedBodyHttpServletRequest): AuthorizationResult
}

sealed interface AuthorizationResult {
    companion object {
        fun success(): AuthorizationResult = Success

        fun failure(message: String): AuthorizationResult = Failure(message)
    }

    data object Success : AuthorizationResult

    class Failure(val message: String) : AuthorizationResult
}

private class AlwaysAuthorizedAuthorizationFilter(objectMapper: ObjectMapper) :
    DataOpennessAuthorizationFilter(objectMapper) {
    override fun isAuthorizedForEndpoint(request: CachedBodyHttpServletRequest) = AuthorizationResult.success()
}

private class ProtectedDataAuthorizationFilter(
    objectMapper: ObjectMapper,
    private val accessKeys: AccessKeys,
    private val fieldsThatServeNonAggregatedData: List<String>,
) :
    DataOpennessAuthorizationFilter(objectMapper) {
    companion object {
        private val WHITELISTED_PATH_PREFIXES = listOf("/swagger-ui", "/api-docs")
        private val ENDPOINTS_THAT_SERVE_AGGREGATED_DATA = listOf(
            AGGREGATED_ROUTE,
            NUCLEOTIDE_MUTATIONS_ROUTE,
            AMINO_ACID_MUTATIONS_ROUTE,
            NUCLEOTIDE_INSERTIONS_ROUTE,
            AMINO_ACID_INSERTIONS_ROUTE,
        )
    }

    override fun isAuthorizedForEndpoint(request: CachedBodyHttpServletRequest): AuthorizationResult {
        val isOperatedBehindAProxy = !request.contextPath.isNullOrBlank()
        val path = when {
            isOperatedBehindAProxy -> request.servletPath
            else -> request.requestURI
        }

        if (path == "/" || WHITELISTED_PATH_PREFIXES.any { path.startsWith(it) }) {
            return AuthorizationResult.success()
        }

        val requestFields = request.getRequestFields()

        val accessKey = requestFields[ACCESS_KEY_PROPERTY]?.textValue()
            ?: return AuthorizationResult.failure("An access key is required to access $path.")

        if (accessKeys.fullAccessKey == accessKey) {
            return AuthorizationResult.success()
        }

        val endpointServesAggregatedData = ENDPOINTS_THAT_SERVE_AGGREGATED_DATA.contains(path) &&
            fieldsThatServeNonAggregatedData.intersect(requestFields.keys).isEmpty()

        if (endpointServesAggregatedData && accessKeys.aggregatedDataAccessKey == accessKey) {
            return AuthorizationResult.success()
        }

        return AuthorizationResult.failure("You are not authorized to access $path.")
    }
}
