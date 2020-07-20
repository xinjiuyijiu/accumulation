# HTTP的请求
![title](https://raw.githubusercontent.com/xinjiuyijiu/NoteImages/master/gitnote/2020/07/20/http_request-1595211663915.jpg)

cr：carriage return 回车
lf：line feed 换行
sp：space 空格

## HTTP请求的格式

|**请求行**| *方法*（空格）*URL*（空格）*HTTP版本*（回车换行）|
|-|-|-|
|**首部**| key1（空格）value1（回车换行）|
|-|  key2（空格）value2（回车换行）|
|更多的header|......|
|**(回车换行)** |-|
|**request body**|正文|

- 八种请求方法：GET，POST，PUT，PATCH，DELETE，TRACE，OPTIONS，CONNECT
- URL统一资源定位符：
- 首部字段：
     - ACCEPT-TYPE：客户端可以接收的
     - CONTENT-TYPE：正文的格式
     - 缓存
- 正文：

## HTTP响应的格式
![title](https://raw.githubusercontent.com/xinjiuyijiu/NoteImages/master/gitnote/2020/07/20/http_response-1595224121809.jpg)

|**状态行**|*HTTP版本*（空格）*状态码*（空格）*状态码描述*（回车换行）|
|-|-|
|**首部**| key1（空格）value1（回车换行）|
|-|  key2（空格）value2（回车换行）|
|**(回车换行)** |-|
|**实体**|正文|

- 状态码&状态码原因短语
  - 1XX：信息状态码&接收的请求正在处理
  - 2XX：成功状态码&请求正常处理完毕
  - 3XX：重定向状态码&需要进行附加操作以完成请求
  - 4XX：客户端错误状态码&服务器无法处理请求
  - 5XX：服务器错误状态码&服务器处理请求错误
  
- 状态码描述：状态码的原因短语
- 首部字段
  - Content-Type：响应实体的类型
  - Content-Length：响应实体的字节长度
  

# HTTP/1.1

# HTTP/2.0




# HTTP/3.0
QUIC（quick udp internet connection）


HTTP1.1和HTTP2.0是基于TCP协议，HTTP3.0基于UDP协议；

# HTTP