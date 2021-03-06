package cn.bigdata

import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.{CloseableHttpResponse, HttpPost}
import org.apache.http.entity.{ByteArrayEntity, StringEntity}
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils

object HttpClient {

  def main(args: Array[String]): Unit=
  {

    val url = "http://2.0.1.1:8001/test"
    val timeout = 18000

    val requestConfig = RequestConfig.custom()
    .setConnectTimeout(timeout * 1000)
    .setConnectionRequestTimeout(timeout * 1000)
    .setSocketTimeout(timeout * 1000).build()

    import scala.io.Source
    val source = Source.fromFile("E:\\project\\sparktest\\src\\main\\resource\\group.json","UTF-8")

    val json = source.getLines().toArray.mkString("\n")

    val client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build()


    val post: HttpPost = new HttpPost(url)
     post.addHeader("Content-Type", "application/json")
     post.setEntity(new StringEntity(json))



    val response: CloseableHttpResponse = client.execute(post)

    val entity = response.getEntity

    val str = EntityUtils.toString(entity, "UTF-8")
     println ("Code is " + str)

  }


}

