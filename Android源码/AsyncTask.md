AsyncTask 异步任务
内部创建一个ThreadPoolExecutor线程池，对Thread和Handler的封装
串行和并行执行：
两个executor：
- THREAD_POOL_EXECUTOR
- SERIAL_EXECUTOR

缺陷：
生命周期为绑定，导致内存泄露
cancel不一定成功，导致内存泄露- doInBackground可能无法被中断，导致无法cancel


Android异步工具：
Thread+Handler ：实现复杂
AsyncTask：可能的内存泄露
Loader
