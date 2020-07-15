
# TCP
![title](https://raw.githubusercontent.com/xinjiuyijiu/NoteImages/master/gitnote/2020/07/15/tcp_protocol-1594792406350.jpg)

序号：解决有序问题
确认序号：解决了到达问题，也就是丢包问题
窗口大小：解决了流量控制
syn等状态位：解决连接状态


## 流量控制：
 接收方通过发送ack数据，其中包含了滑动窗口的大小，从而告诉发送方发送数据的快慢；如果接收方无法及时处理发送方的大量数据，导致数据丢失，从而会触发重发机制；为了匹配发送方发送数据的速度和接收方接受数据的速度；

## 拥塞控制：
路由器无法处理高速到达的流量而丢弃数据的现象称为拥塞；因此，发送方不仅需要考虑接收方的流量控制，还需要考虑网络环境，导致的拥堵问题，因此需要通过拥塞窗口进一步控制发送方的发送速率；

流量控制是接收端支配发送端发送速率；拥塞控制，是发送端感知网络环境后的自我调节发送速率；

## 三次握手
![title](https://raw.githubusercontent.com/xinjiuyijiu/NoteImages/master/gitnote/2020/07/15/tcp_three_handshake-1594801162383.jpg)

客户端和服务器端都处于CLOSED状态；服务器端开启端口监听，处于LISTEN状态；**第一次握手**，客户端发起SYN连接请求，处于SYN_SENT状态；**第二次握手**，服务器端收到SYN连接请求后，返回SYN连接请求，以及ACK回复，处于SYN_RCVD状态；**第三次握手**，客户端收到服务器端的SYN和ACK后，发送ACK和ACK，处于ESTABLISHED状态；服务器端收到客户端的ACK后，处于ESTABLISHED状态；

## 四次挥手
![title](https://raw.githubusercontent.com/xinjiuyijiu/NoteImages/master/gitnote/2020/07/15/tcp_four_closed-1594802278575.jpg)

A,B两端处于TCP连接已建立的状态；此时A发送FIN，主动断开连接，进入FIN-WAIT-1状态；B收到断开连接的请求后，回复ACK，进入CLOSED-WAIT状态；A收到B的ACK后，进入FIN-WAIT-2状态；B收到断开请求后，表示也可以断开请求了，向A发送FIN，ACK
