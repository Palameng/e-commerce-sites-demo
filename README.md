# e-commerce-sites-demo  （施工中）
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
这里的接口返回都为json格式。后台管理测试需要走登录，接口为：  
（POST请求,BODY里填充username=admin,password=admin）http://47.107.39.251/user/login.do
## 后台部分
### 1 后台商品管理
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

### 2 订单后台管理
#### 2.1 发货动作
http://47.107.39.251/manage/order/send_goods.do?orderNo=1492091141269  
#### 2.2 所有订单列表
http://47.107.39.251/manage/order/list.do  
#### 2.3 订单详情
http://47.107.39.251/manage/order/detail.do?orderNo=1533534085285  
#### 2.4 搜索订单
http://47.107.39.251/manage/order/search.do?orderNo=1533534085285  

### 3 商品类别后台管理
#### 3.1 增加类别
http://localhost:8088/manage/category/add_category.do?parentId=0&categoryName="A"  
#### 3.2 获取单层子类别
不加参数categoryId则默认为顶级目录  
http://47.107.39.251/manage/category/get_category.do  
查看顶级类别ID=100001下的子类别  
http://47.107.39.251/manage/category/get_category.do?categoryId=100001  
#### 3.3 获取某目录下子类别树
该接口只返回id不返回具体内容，不加参数categoryId则默认获取所有类别id  
http://47.107.39.251/manage/category/get_deep_category.do?  
查看id=100001下的所有子类别id（包括返回自己的id组成一系列结点）  
http://47.107.39.251/manage/category/get_deep_category.do?categoryId=100001  
#### 3.4 设置类别名称
http://47.107.39.251/manage/category/set_category_name.do?categoryId=100031&categoryName="D"  

## 前台部分
前台部分可以直接访问。  
### 4 商品管理
#### 4.1 商品详情
http://47.107.39.251/product/detail.do?productId=28  
#### 4.2 商品列表
http://47.107.39.251/product/list.do?keyword=i&orderBy=price_desc  

### 5 收货地址管理
#### 5.1 添加收货地址
http://47.107.39.251/shipping/add.do?userId=1&receiverName=meng&receiverPhone=11111&receiverMobile=18688888888&receiverProvince=广西&receiverCity=南宁市&receiverAddress=广场公寓&receiverZip=100000  
#### 5.2 删除收货地址
http://47.107.39.251/shipping/del.do?shippingId=30  
#### 5.3 收货地址列表
http://47.107.39.251/shipping/list.do  
#### 5.4 选中收货地址
http://47.107.39.251/shipping/select.do?shippingId=29  
#### 5.5 更新收货地址
http://47.107.39.251/shipping/update.do?id=29&receiverName=meng&receiverPhone=11111&receiverMobile=18688888888&receiverProvince=广西&receiverCity=南宁市&receiverAddress=广场公寓&receiverZip=100000  

### 6 订单管理
生成订单都是根据已有信息拼凑，这里只用传收货地址id，service层实现是先获取到选中的cart记录，购物车每个cart记录商品id和数量和所属人，之后对具体商品的库存和售卖状态进行判断后封装成一个orderItem，orderItem记录每个商品条目的详细信息，几个商品条目item就组合成一个order。  
####  6.1 生成订单
http://47.107.39.251/order/create.do?shippingId=29  
####  6.2 取消订单
http://47.107.39.251/order/cancel.do?orderNo=1533534085285  
####  6.3 订单详情
http://47.107.39.251/order/detail.do?orderNo=1533534085285  
####  6.4 获取购物车下的订单里的商品列表，即一个OrderItemList
http://47.107.39.251/order/get_order_cart_product.do  
####  6.5 订单列表
http://47.107.39.251/order/list.do  
####  6.6 支付订单（这里需要结合实际情况测试）
http://47.107.39.251/order/pay.do?orderNo=1492091102371  
####  6.7 查询订单支付状态（这里需要结合实际情况测试）
http://47.107.39.251/order/query_order_pay_status.do?orderNo=1492091102371  

### 7 购物车管理
这里维护一个商品ID和数量的内容。  
#### 7.1 添加购物车
http://localhost:8088/cart/add.do?productId=26&count=10    
#### 7.2 删除购物车中的商品
http://localhost:8088/cart/delete_product.do?productIds=26  
#### 7.3 获取购物车商品数
http://localhost:8088/cart/get_cart_product_count.do  
#### 7.4 购物车中商品列表
http://localhost:8088/cart/list.do  
#### 7.5 选中购物车中的商品
http://localhost:8088/cart/select.do?productId=28  
#### 7.6 全选
http://localhost:8088/cart/select_all.do  
#### 7.7 反选
http://localhost:8088/cart/un_select.do?productId=28  
#### 7.8 全不选
http://localhost:8088/cart/un_select_all.do  
#### 7.9 更新购物车中的商品信息
http://localhost:8088/cart/update.do?productId=26&count=1  














