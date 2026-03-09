package error

import kotlinx.serialization.Serializable

@Serializable
data class ServerStatusCode(val value: Int, val description: String) {

    companion object {

        public val Continue: ServerStatusCode = ServerStatusCode(100, "Continue")
        public val SwitchingProtocols: ServerStatusCode = ServerStatusCode(101, "Switching Protocols")
        public val Processing: ServerStatusCode = ServerStatusCode(102, "Processing")

        public val OK: ServerStatusCode = ServerStatusCode(200, "OK")
        public val Created: ServerStatusCode = ServerStatusCode(201, "Created")
        public val Accepted: ServerStatusCode = ServerStatusCode(202, "Accepted")

        public val NonAuthoritativeInformation: ServerStatusCode =
            ServerStatusCode(203, "Non-Authoritative Information")

        public val NoContent: ServerStatusCode = ServerStatusCode(204, "No Content")
        public val ResetContent: ServerStatusCode = ServerStatusCode(205, "Reset Content")
        public val PartialContent: ServerStatusCode = ServerStatusCode(206, "Partial Content")
        public val MultiStatus: ServerStatusCode = ServerStatusCode(207, "Multi-Status")

        public val MultipleChoices: ServerStatusCode = ServerStatusCode(300, "Multiple Choices")
        public val MovedPermanently: ServerStatusCode = ServerStatusCode(301, "Moved Permanently")
        public val Found: ServerStatusCode = ServerStatusCode(302, "Found")
        public val SeeOther: ServerStatusCode = ServerStatusCode(303, "See Other")
        public val NotModified: ServerStatusCode = ServerStatusCode(304, "Not Modified")
        public val UseProxy: ServerStatusCode = ServerStatusCode(305, "Use Proxy")
        public val SwitchProxy: ServerStatusCode = ServerStatusCode(306, "Switch Proxy")
        public val TemporaryRedirect: ServerStatusCode = ServerStatusCode(307, "Temporary Redirect")
        public val PermanentRedirect: ServerStatusCode = ServerStatusCode(308, "Permanent Redirect")

        public val BadRequest: ServerStatusCode = ServerStatusCode(400, "Bad Request")
        public val Unauthorized: ServerStatusCode = ServerStatusCode(401, "Unauthorized")
        public val PaymentRequired: ServerStatusCode = ServerStatusCode(402, "Payment Required")
        public val Forbidden: ServerStatusCode = ServerStatusCode(403, "Forbidden")
        public val NotFound: ServerStatusCode = ServerStatusCode(404, "Not Found")
        public val MethodNotAllowed: ServerStatusCode = ServerStatusCode(405, "Method Not Allowed")
        public val NotAcceptable: ServerStatusCode = ServerStatusCode(406, "Not Acceptable")

        public val ProxyAuthenticationRequired: ServerStatusCode =
            ServerStatusCode(407, "Proxy Authentication Required")

        public val RequestTimeout: ServerStatusCode = ServerStatusCode(408, "Request Timeout")
        public val Conflict: ServerStatusCode = ServerStatusCode(409, "Conflict")
        public val Gone: ServerStatusCode = ServerStatusCode(410, "Gone")
        public val LengthRequired: ServerStatusCode = ServerStatusCode(411, "Length Required")
        public val PreconditionFailed: ServerStatusCode = ServerStatusCode(412, "Precondition Failed")
        public val PayloadTooLarge: ServerStatusCode = ServerStatusCode(413, "Payload Too Large")
        public val RequestURITooLong: ServerStatusCode = ServerStatusCode(414, "Request-URI Too Long")

        public val UnsupportedMediaType: ServerStatusCode = ServerStatusCode(415, "Unsupported Media Type")

        public val RequestedRangeNotSatisfiable: ServerStatusCode =
            ServerStatusCode(416, "Requested Range Not Satisfiable")

        public val ExpectationFailed: ServerStatusCode = ServerStatusCode(417, "Expectation Failed")
        public val UnprocessableEntity: ServerStatusCode = ServerStatusCode(422, "Unprocessable Entity")
        public val Locked: ServerStatusCode = ServerStatusCode(423, "Locked")
        public val FailedDependency: ServerStatusCode = ServerStatusCode(424, "Failed Dependency")
        public val TooEarly: ServerStatusCode = ServerStatusCode(425, "Too Early")
        public val UpgradeRequired: ServerStatusCode = ServerStatusCode(426, "Upgrade Required")
        public val TooManyRequests: ServerStatusCode = ServerStatusCode(429, "Too Many Requests")

        public val RequestHeaderFieldTooLarge: ServerStatusCode =
            ServerStatusCode(431, "Request Header Fields Too Large")

        public val InternalServerError: ServerStatusCode = ServerStatusCode(500, "Internal Server Error")
        public val NotImplemented: ServerStatusCode = ServerStatusCode(501, "Not Implemented")
        public val BadGateway: ServerStatusCode = ServerStatusCode(502, "Bad Gateway")
        public val ServiceUnavailable: ServerStatusCode = ServerStatusCode(503, "Service Unavailable")
        public val GatewayTimeout: ServerStatusCode = ServerStatusCode(504, "Gateway Timeout")

        public val VersionNotSupported: ServerStatusCode =
            ServerStatusCode(505, "HTTP Version Not Supported")

        public val VariantAlsoNegotiates: ServerStatusCode = ServerStatusCode(506, "Variant Also Negotiates")
        public val InsufficientStorage: ServerStatusCode = ServerStatusCode(507, "Insufficient Storage")

    }
}