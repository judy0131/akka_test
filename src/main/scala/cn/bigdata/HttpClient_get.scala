package cn.bigdata

import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.{CloseableHttpResponse, HttpGet, HttpPost}
import org.apache.http.entity.ByteArrayEntity
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils

object HttpClient_get {

  def main(args: Array[String]): Unit=
  {

    val url = "http://10.31.0.27:8002/test"
    val timeout = 18000

    val requestConfig = RequestConfig.custom()
    .setConnectTimeout(timeout * 1000)
    .setConnectionRequestTimeout(timeout * 1000)
    .setSocketTimeout(timeout * 1000).build()

   val json="hello !!!!!!!!!11hello !!!!!!!!!11hello !!!!!!!!!11hello !!!!!!!!!11hello !!!!!!!!!11hello !!!!!!!!!11hello !!!!!!!!!11"

    val client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build()


    val get: HttpGet = new HttpGet(url)

    val response: CloseableHttpResponse = client.execute(get)

    val entity = response.getEntity

    val str = EntityUtils.toString(entity, "UTF-8")
     println ("Code is " + str)

  }


}

