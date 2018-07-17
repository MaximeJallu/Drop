import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset


private val UTF8 = Charset.forName("UTF-8")
private const val SINGLE_DIVIDER = "────────────────────────────────"

class CurlLogInterceptor(private val logger: Logger?, _tag: String = "CURL") : Interceptor {

    private val tag = if (_tag.startsWith("CURL")) {
        _tag
    } else {
        "CURL-$_tag"
    }
    private var curlCommandBuilder = StringBuilder("")

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (request.body() != null) {
            curlCommandBuilder = StringBuilder("")
            // add cURL command
            curlCommandBuilder.append("cURL ")
            curlCommandBuilder.append("-X ")
            // add method
            curlCommandBuilder.append("${request.method().toUpperCase()} ")
            // adding headers
            for (headerName in request.headers().names()) {
                addHeader(headerName, request.headers().get(headerName)!!)
            }

            // adding request body
            val requestBody = request.body()!!
            if (request.body() != null) {
                val buffer = Buffer()
                requestBody.writeTo(buffer)
                var charset = UTF8
                val contentType = requestBody.contentType()
                if (contentType != null) {
                    addHeader("Content-Type", request?.body()!!.contentType().toString())
                    charset = contentType.charset(UTF8)
                    curlCommandBuilder.append(" -d '${buffer.readString(charset)}'")
                }
            }

            // add request URL
            curlCommandBuilder.append(" \"${request.url()}\"")
            curlCommandBuilder.append(" -L")

            print(request.url().toString(), curlCommandBuilder.toString())
        }
        return chain.proceed(request)
    }

    private fun addHeader(headerName: String, headerValue: String) {
        curlCommandBuilder.append("-H \"$headerName: $headerValue\" ")
    }


    private fun print(url: String, msg: String) {
        val logMsg = StringBuilder("\n")
        logMsg.append("\n")
        logMsg.append("URL: $url")
        logMsg.append("\n")
        logMsg.append(SINGLE_DIVIDER)
        logMsg.append("\n")
        logMsg.append(msg)
        logMsg.append(" ")
        logMsg.append(" \n")
        logMsg.append(SINGLE_DIVIDER)
        logMsg.append(" \n ")

        log(logMsg.toString())
    }

    private fun log(msg: String) {
        if (logger != null) {
            logger.print(tag, msg)
        } else {
            Log.i(tag, msg)
        }
    }

}

interface Logger {
    fun print(tag: String, msg: String)
}
