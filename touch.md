### touch 事件

1. 如果仅仅是对一个 view 实现触摸反馈，重写 onTouch 即可
2. 如果是对于一个 ViewGroup 比如 ，ListView#ItemLayout 实现长按重排 item ,
那么在长按之后，滑动 item 的时候，就需要让父不要拦截，自己滑动，滑动完了，应该要 listView.layoutChildren()
3. 如果 viewGroup 要滑动，但是 view 可以点击。那么在 viewGroup#onIntercept 里，
要判断当前是否滑动了，在滑动的时候拦截，并 scrollBy 一下自己

