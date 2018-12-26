```java
//  获取touchSlop （系统 滑动距离的最小值，大于该值可以认为滑动）
int touchSlop = viewConfiguration.getScaledTouchSlop();
//  获得允许执行fling （抛）的最小速度值
int minimumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
//  获得允许执行fling （抛）的最大速度值
int maximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
//  Report if the device has a permanent menu key available to the user
//  （报告设备是否有用户可找到的永久的菜单按键）
//  即判断设备是否有返回、主页、菜单键等实体按键（非虚拟按键）
boolean hasPermanentMenuKey = viewConfiguration.hasPermanentMenuKey();

//  获得敲击超时时间，如果在此时间内没有移动，则认为是一次点击
int tapTimeout =  ViewConfiguration.getTapTimeout();
//  双击间隔时间，在该时间内被认为是双击
int doubleTapTimeout = ViewConfiguration.getDoubleTapTimeout();
//  长按时间，超过此时间就认为是长按
int longPressTimeout = ViewConfiguration.getLongPressTimeout();
//  重复按键间隔时间
int repeatTimeout = ViewConfiguration.getKeyRepeatTimeout();
---------------------
作者：yinxtno1
来源：CSDN
原文：https://blog.csdn.net/yinxtno1/article/details/76167951
版权声明：本文为博主原创文章，转载请附上博文链接！

```
---------------------
作者：yinxtno1
来源：CSDN
原文：https://blog.csdn.net/yinxtno1/article/details/76167951
版权声明：本文为博主原创文章，转载请附上博文链接！