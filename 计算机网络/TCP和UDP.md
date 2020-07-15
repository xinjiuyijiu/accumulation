
# TCP
![title](https://raw.githubusercontent.com/xinjiuyijiu/NoteImages/master/gitnote/2020/07/15/tcp_protocol-1594792406350.jpg)

序号：解决有序问题
确认序号：解决了到达问题，也就是丢包问题
窗口大小：解决了流量控制
syn等状态位：解决连接状态


流量控制：
 接收方通过发送ack数据，其中包含了滑动窗口的大小，从而告诉发送方发送数据的快慢；
 为了匹配发送方发送数据的速度和接收方接受数据的速度；

拥塞控制：路由器无法处理高速到达的流量而丢弃数据的
三次握手
四次挥手
