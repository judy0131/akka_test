package cn.bigdata

import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.{CloseableHttpResponse, HttpPost}
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils

object HttpClient_small {

  def main(args: Array[String]): Unit=
  {

    val url = "http://10.31.0.27:8001/test"
    val timeout = 18000

    val requestConfig = RequestConfig.custom()
    .setConnectTimeout(timeout * 1000)
    .setConnectionRequestTimeout(timeout * 1000)
    .setSocketTimeout(timeout * 1000).build()

   val json="hello !!!!!!!!!11"

    val client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build()


    val post: HttpPost = new HttpPost(url)
     post
    .addHeader("Content-Type", "application/json")
     post
    .setEntity(new StringEntity(json))



    val response: CloseableHttpResponse = client.execute(post)

    val entity = response.getEntity

    val str = EntityUtils.toString(entity, "UTF-8")
     println ("Code is " + str)

  }


}

