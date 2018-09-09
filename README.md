# e-commerce-sites-demo  
该项目为个人学习慕课网课程学习记录。  
  
## 问题记录  
### 1 Idea在pom.xml中添加了mybatis-generator插件后，右侧的maven projects中并未显示这个插件。  
解决方法: 新建一个plugins标签和pluginManager同级，然后把generator插件配置放进去。  
### 2 数据库配置驱动路径报错提示未找到。  
解决方法：注意，如果从maven本地仓库拉取时，后缀不带有bin，所以如果配置的时候要检查清楚文件名和所配路径。  
### 3 Field injection is not recommended 引出的关于antowired的三中配置方法哪一种更好？  
https://blog.csdn.net/github_38222176/article/details/79506392  
### 4 @Autowired的用法详解  
https://blog.csdn.net/u013257679/article/details/52295106/  
### 5 expected at least 1 bean which qualifies as autowire candidate for this dependency 的异常。  
解决方法：  
1）检查各种注解是否添加，spring扫描包路径是否正确；  
2）如果修改了包名或者项目名，确保pom文件中build标签和一些关于修改文件名的标签内容匹配；  
3）spring的版本和ide不兼容；  
4）尝试降低Autowired的警告级别，因为上述第三点中提示有Spring团队不提倡直接在内部属性上加autowired。  
### 6 PageInfo处理分页结果的时候为什么不直接使用volist而先要用dao层的请求结果传入？  
http://coding.imooc.com/learn/questiondetail/10968.html  


## 其他操作  
### 1 去除sql黄色辣眼睛背景  
https://www.cnblogs.com/prettrywork/p/7904378.html  
### 2 Guava Cache学习  
https://blog.csdn.net/u012881904/article/details/79263787  
### 3 模糊查询的一些技巧  
https://bbs.csdn.net/topics/320222905  

## 测试说明
这里的接口返回都为json格式。
登录接口：  
（POST请求,BODY里填充username=admin,password=admin）http://47.107.39.251/user/login.do
### 1 商品后台管理
#### 1.1 商品添加/更新
更新商品：  
http://47.107.39.251/manage/product/save.do?categoryId=1&name=三星电视机&subtitle=促销&mainImage=abc.jpg&subImages=test.jpg&detail=detailtext&price=1000&stock=100&status=1&id=26  
添加商品：  
http://47.107.39.251/manage/product/save.do?categoryId=1&name=三星电视机&subtitle=促销&mainImage=abc.jpg&subImages=test.jpg&detail=detailtext&price=1000&stock=100&status=1  
#### 1.2 商品详情
http://47.107.39.251/manage/product/detail.do?productId=26  
#### 1.3 商品列表
http://47.107.39.251/manage/product/list.do  
#### 1.4 商品模糊查找
http://47.107.39.251/manage/product/search.do?productName=美的  
#### 1.5 设置售卖状态
http://47.107.39.251/manage/product/set_sale_status.do?productId=26&status=2  


