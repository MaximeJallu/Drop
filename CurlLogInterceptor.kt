import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset

class CurlLogInterceptor : Interceptor {
    companion object {
        private val UTF8 = Charset.forName("UTF-8")
        private const val SINGLE_DIVIDER = "────────────────────────────────────────────────────────────────"
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (!BuildConfig.DEBUG) {
            return chain.proceed(request)
        }

        val logBuilder = StringBuilder("curl -X ${request.method().toUpperCase()} ")

        // adding headers
        for (headerName in request.headers().names()) {
            addHeader(headerName, request.headers()[headerName])?.also {
                logBuilder.append(it)
            }
        }
        request.body()?.also {
            // adding request body
            val buffer = Buffer()
            it.writeTo(buffer)

            val contentType = it.contentType()
            if (contentType != null) {
                addHeader("Content-Type", it.contentType().toString())
                logBuilder.append(" -d '${buffer.readString(UTF8)}'")
            }
        }
        // add request URL
        logBuilder.appendln().append(" \"${request.url()}\"")
        logBuilder.append(" -L")

        print(request.url().toString(), logBuilder.toString())

        return chain.proceed(request)
    }

    private fun addHeader(headerName: String, headerValue: String?): String? {
        return try {
            headerValue?.let {
                "-H \"$headerName: $it\" "
            }
        } catch (e: ArrayIndexOutOfBoundsException) {
            log(e.message ?: "CURL - ArrayIndexOutOfBoundsException")
            null
        }
    }

    private fun print(url: String, msg: String) {
        val logMsg = StringBuilder("\n").apply {
            append(SINGLE_DIVIDER)
            appendln()
            append("Request ID: " + this@CurlLogInterceptor.toString().substringAfterLast("."))
            appendln()
            append("URL cible: $url")
            appendln()
            appendln()
            append(msg)
            appendln()
            append(SINGLE_DIVIDER)
            appendln()
        }
        log(logMsg.toString().replace("-H", "\r\n-H"))
    }

    private fun log(msg: String) {
        Log.i(TAG, msg)
    }
}
