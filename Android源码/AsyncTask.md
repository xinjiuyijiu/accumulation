AsyncTask 异步任务
内部创建一个ThreadPoolExecutor线程池，对Thread和Handler的封装
串行和并行执行：
两个executor：
- THREAD_POOL_EXECUTOR
- SERIAL_EXECUTOR