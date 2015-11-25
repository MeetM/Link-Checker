import scala.concurrent._
import com.ning.http.client.AsyncHttpClient
import java.util.concurrent.Executor


object WebClient {
  private val client = new AsyncHttpClient
 
  def get(url : String)(implicit exec : Executor) : Future[String] = {
    val p = Promise[String]()
    val f = client.prepareGet(url).execute();
    
    f.addListener(new Runnable {
      def run = {
        val response = f.get
        if (response.getStatusCode < 400)
          p.success(response.getResponseBodyExcerpt(131072))
          else p.failure(new java.lang.RuntimeException())
      }
    }, exec);
    
    p.future
  }
}