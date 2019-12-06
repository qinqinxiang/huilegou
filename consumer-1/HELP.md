# spring boot 2.2.1 整合问题
  1.整合actuator 访问localhost:9000/info 页面404 原因是2.0的将所有的端点都屏蔽了 
    需要加localhost:9000/actuator/info 或者降低spring boot版本
