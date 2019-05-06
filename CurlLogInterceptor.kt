import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset


private val UTF8 = Charset.forName("UTF-8")
private const val SINGLE_DIVIDER = "────────────────────────────────"

class CurlLogInterceptor(private val logger: Logger? = null, _tag: String = "CURL") : Interceptor {

    private val tag = if (_tag.startsWith("CURL")) {
        _tag
    } else {
        "CURL-$_tag"
    }
    private var curlCommandBuilder = StringBuilder("")

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        curlCommandBuilder = StringBuilder("")
        // add cURL command
        curlCommandBuilder.append("cURL ")
        curlCommandBuilder.append("-X ")
        // add method
        curlCommandBuilder.append("${request.method().toUpperCase()} ")
        // adding headers
        for (headerName in request.headers().names()) {
            addHeader(headerName, request.headers()[headerName])
        }
        request.body()?.also {
            // adding request body
            if (request.body() != null) {
                val buffer = Buffer()
                it.writeTo(buffer)
                val charset = UTF8
                val contentType = it.contentType()
                if (contentType != null) {
                    addHeader("Content-Type", it.contentType().toString())
                    curlCommandBuilder.append(" -d '${buffer.readString(charset)}'")
                }
            }
        }
        // add request URL
        curlCommandBuilder.append(" \"${request.url()}\"")
        curlCommandBuilder.append(" -L")

        print(request.url().toString(), curlCommandBuilder.toString())

        return chain.proceed(request)
    }

    private fun addHeader(headerName: String, headerValue: String?) {
        try {
            headerValue?.also {
                curlCommandBuilder.append("-H \"$headerName: $it\" ")
            }
        } catch (e: ArrayIndexOutOfBoundsException) {
            log(e.message ?: "CURL - ArrayIndexOutOfBoundsException")
        }
    }

    private fun print(url: String, msg: String) {
        val logMsg = StringBuilder("\n").apply {
            appendln()
            append("URL: $url")
            appendln()
            append(SINGLE_DIVIDER)
            appendln()
            append(msg)
            append(" ")
            appendln()
            append(SINGLE_DIVIDER)
            appendln()
        }

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
