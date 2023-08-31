# PhotoShare

![PhotoShare-导出](./PhotoShare-导出 (1).png)

----

### MainActivity

布局分为3块：

* HomeFragment：帖子页面
* PublishFragment：发布帖子页面
* UserFragment：用户个人中心页面

切换页面通过底部导航栏切换，具体逻辑为：

~~~java
// 事先初始化好HomeFragment
private void initHomeFragment() {
    manager = getSupportFragmentManager();
    homeFragment = HomeFragment.newInstance();
    manager.beginTransaction()
            .add(R.id.home_fragment_container, homeFragment)
            .commit();
}
// UserFragment需要经过用户登陆完成才运行访问
// 帖子发布界面可以直接初始化
// 回调函数使用见MainActivity的changeActivityHandler()方法第66行
@Override
public void onSuccess(String id) {
    if (!id.equals(resourcesUtils.ID)) {
        // 校验成功则生成Fragment
        SharedPreferencesUtils.saveString(MainActivity.this, resourcesUtils.ID, id);
    }
    userFragment = UserFragment.newInstance();
}
// 底部导航栏绑定点击事件
private void initNavigation() {
    BottomNavigationView mNavigationView = findViewById(R.id.navigation_bottom);
    mNavigationView.setOnItemSelectedListener(item -> {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_home) {
            return replaceFragment(homeFragment);
        } else if (itemId == R.id.menu_add) {
            return replaceFragment(publishFragment);
        } else if (itemId == R.id.menu_user) {
            return replaceFragment(userFragment);
        }
        return false;
    });
}
// 切换方法
public boolean replaceFragment(Fragment fragment) {
	if (fragment != null) {
		manager.beginTransaction()
			.replace(R.id.home_fragment_container, fragment)
			.commit();
		return true;
	}
	return false;
}
~~~

判断是否登录的逻辑：

~~~java
private void changeActivityHandler() {
    String userIdKey = resourcesUtils.ID;
    // 如果在本地已经保存有用户的信息,则进行校验
    User user = SharedPreferencesUtils.getUser(MainActivity.this);
    // 用userId判断登陆状态
    String userId = SharedPreferencesUtils.getString(MainActivity.this, userIdKey, null);
    if (userId != null && !userId.equals("")) {
        // 对登陆状态进行校验，等待回调即可
        // 最后一个传参就是传的回调函数，见实现的接口
        UserStateUtils.userInformationIsOkHandler(user, MainActivity.this, this);
    } else {
        Intent intent = new Intent(MainActivity.this, EntranceActivity.class);
        startActivity(intent);
        finish();
    }
}
~~~

----

#### HomeFragment

帖子页面需要在渲染时就有数据展示给用户，因此，需要一加载就发请求获取数据。

~~~java
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_home, container, false);
    initView(rootView); // 渲染页面
    getShare(); // 获取帖子数据
    return rootView;
}
// 下面是获取帖子数据
public void getShare() {
    // 获取存储在本地的用户id，登陆后获取
    String id = SharedPreferencesUtils.getString(getContext(), resourcesUtils.ID, null);
    HashMap<String, String> params = new HashMap<>();
    params.put("userId", id);
    // 发送请求
    HttpUtils.sendGetRequest(Api.SHARE, params, new Callback() {
        // 成功回调
        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            // 省略了一些判断操作
                    record = result.getData();
                    // 这里关键，在执行回调时，这里在安卓中不认为是渲染UI的线程，因此不允许设置RecycleView的适配器
                    // 所以需要下面方法获取主线程(UI渲染线程)来操作
                    new Handler(Looper.getMainLooper()).post(() -> {
                        homeAdapter = new HomeAdapter(getContext(), record, HomeFragment.this);
                        HomeRecyclerList.setAdapter(homeAdapter);
        }
    });
}
// 关于RecycleView：
public void initView(View rootView) {
    HomeRecyclerList = rootView.findViewById(R.id.recyclerListHome);
    // RecycleView需要设置布局管理器来布局
    HomeRecyclerList.setLayoutManager(new LinearLayoutManager(getContext()));
    homeAdapter = null;
}
~~~

然后实践发现，经过点赞、收藏后帖子的信息已经发生改变，需要及时改变

因此，实现接口`onChangePostState`
，在实例化适配器时顺带放进去当回调，当执行点赞、收藏时进行回调，然后调用适配器的`updateData()`
方法修改数据，最后让适配器重新渲染即可。

~~~java
@Override
public void onChangePostState(int position) {
    // 省略传参
    HttpUtils.sendGetRequest(Api.SHARE, params, new Callback() {
		// 省略失败回调
        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        	// 省略一堆判断
        	new Handler(Looper.getMainLooper()).post(() -> {
            	        record = result.getData();
                		// 重新设置数据
                	    homeAdapter.updateData(record);
                		// 适配器重新渲染position位置的内容
                    	homeAdapter.notifyItemChanged(position);
                	});
        }
    });
}
~~~

`HomeAdapter`内容看代码，没什么好说

##### PostInformationActivity

帖子详情页

可以从`HomeFragment`、`收藏页`、`发布页`、`保存页`跳转过来

这里布局逻辑较为复杂，首先帖子主题内容的图片需要`RecycleView`渲染，使用`ImageAdapter`。

往下有评论区，分为一级评论和二级评论，一级评论需要动态生成，二级评论页需要动态生成，所以布局就为使用一个`RecycleView`来渲染一级评论，然后这个`RecycleView`的布局中再包裹一个`RecycleView`渲染二级评论。

以上逻辑见`setRecyclerList()`（100行）

~~~java
// 两个变量，一个判断跳转来源从而决定是否显示评论区和评论输入
// 另一个判断是否是第一次渲染
private String from = null;
private boolean isInit = false;
// 第一次渲染见：82行
initView(); // 全部渲染
// 第二次渲染见：84行
commentAdapter.setDetails(commentRecord.getRecords()); // 仅更新数据，部分渲染
~~~

在`PostInformationActivity`中处理一级评论的内容，然后在一级适配器(`PostCommentAdapter`)中，处理二级评论的内容，二级评论适配器(`SecondCommentAdapter`)只负责渲染二级评论。

这里有个比较关键的地方：

* 和渲染位置绑定的适配器和对应跳转内容都需要放到`ViewHolder`(也可以不叫这个名字，但一定是继承了`RecyclerView.ViewHolder`的类)，否则会出现点击事件绑定错误
  * 原因在于在外部的变量不会关联实时position，只能拿到最后一次调用获取位置方法时的位置(可以理解为只能拿到渲染列表数组最后一个下标位置)。

---

#### PublishFragment

这里由于用到的变量是在太多，因此封装成`PublishModel`

首先，发布页面需要用到相册和照相机，在Api29后有了新方法，如下:

~~~java
public void initLauncher() {
    // 相册的启动器
    galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
        if (result != null) {
            File file = FileUtils.getFileFromUri(getContext(), result);
            if (fileList.size() < 9) {
                fileList.add(file);
                adapter.notifyDataSetChanged();
            } else {
                ToastUtils.show(getContext(), "最多上传九张");
            }
        }
    });
    // 拍照的启动器
    cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
        // 拍完照后返回的是布尔值，不然可以直接获得Uri
        if (result) {
            // 这里绕了大弯，其实可以直接获取文件对象放进适配器
            File file = FileUtils.getFileFromUri(getContext(), imageUri);
            if (fileList.size() < 9) {
                fileList.add(file);
                adapter.notifyDataSetChanged();
            } else {
                ToastUtils.show(getContext(), "最多上传九张");
            }
        }
    });
}

// 当点击对应的按钮后执行操作，总之就是打开相册或者照相机
@Override
public void onOpenGallery() {
    galleryLauncher.launch("image/*");
}
@Override
public void onOpenCamera() {
    cameraLauncher.launch(createImageUri(getContext()));
}
// 打开相册需要传入的处理函数，将图片转化为Uri
public Uri createImageUri(Context context) {
    String timeStamp = new SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault()).format(new Date());
    String imageFileName = "IMG_" + timeStamp + ".jpg";
    ContentValues values = new ContentValues();
    values.put(MediaStore.Images.Media.TITLE, imageFileName);
    imageUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    return imageUri;
}
~~~

由于他的发布图片模式是先上传图片，然后上传成功后返回给我们一个`ImageCode`和`ImageUrlList`，`ImageCode`
可以找到上传的那组图片，然后你实际点击发布时是带这个`ImageCode`进去和帖子进行关联的。

所以，得先上传图片，拿到结果后再执行发布或者保存。

这里用到的适配器只是在上传图片后，重新渲染图片展示栏。

----

#### UserFragment

这里没什么好说的，展示个人信息，设置点击事件然后跳转到对应的页面。

可以跳转到：

* MyselfActivity：个人发布帖子一览
* StarActivity：收藏的帖子一览
* UnPublishedActivity：保存但未发布的帖子一览
* UserInformationActivity(UserHomeFragment)：修改个人信息页面
    * 其实这个可以整合成一个，UserHomeFragment是前期写的，后面偷懒全用Activity没改过去。

前三个Activity的都有列表，适配器内容大同小异，基本只有布局不同。

**用Activity来写页面是想偷懒，没什么别的想法**

相同之处：

* 点击渲染的卡片都能跳转到帖子页面

不同之处：

* 从保存但未发布跳转的没有评论区和发布评论的地方

---

### EntranceActivity

登陆注册的活动

主要逻辑为登陆注册、登录完成后跳转到`MainActivity`并保存登录后返回的数据



