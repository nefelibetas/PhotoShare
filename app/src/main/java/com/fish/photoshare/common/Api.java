package com.fish.photoshare.common;

public final class Api {
    public static final String BASIC_URL = "http://47.107.52.7:88";
    // post：创建用户
    public static final String REGISTER = "http://47.107.52.7:88/member/photo/user/register";

    // post：用户登录
    public static final String LOGIN = "http://47.107.52.7:88/member/photo/user/login";

    // post：修改用户信息
    public static final String UPDATE = "http://47.107.52.7:88/member/photo/user/update";

    // get：获取图片分享列表
    public static final String SHARE = "http://47.107.52.7:88/member/photo/share";

    // post：保存图文分享； get：获取已保存的图文分享列表
    public static final String SAVE = "http://47.107.52.7:88/member/photo/share/save";

    // get：获取我的动态图片分享列表
    public static final String MYSELF = "http://47.107.52.7:88/member/photo/share/myself";

    // get：获取单个图文分享的详情
    public static final String DETAIL = "http://47.107.52.7:88/member/photo/share/detail";

    // post：删除图文分享
    public static final String DELETE = "http://47.107.52.7:88/member/photo/share/delete";

    // post：将保存状态修改为发布状态
    public static final String CHANGE = "http://47.107.52.7:88/member/photo/share/change";

    // post：新增图文分享
    public static final String ADD = "http://47.107.52.7:88/member/photo/share/add";

    // post：用户取消对图文分享的点赞
    public static final String LIKE_CANCEL = "http://47.107.52.7:88/member/photo/like/cancel";

    // post：用户对图文分享进行点赞； get：获取当前登录用户点赞图文列表
    public static final String LIKE = "http://47.107.52.7:88/member/photo/like";

    // post：上传文件
    public static final String UPLOAD = "http://47.107.52.7:88/member/photo/image/upload";

    // post：取消关注
    public static final String FOCUS_CANCEL = "http://47.107.52.7:88/member/photo/focus/cancel";

    // post： 添加关注； get：获取当前登录用户已关注的图文列表
    public static final String FOCUS = "http://47.107.52.7:88/member/photo/focus";

    // get：获取当前登录用户收藏图文列表; post：用户对图文分享进行收藏
    public static final String COLLECT = "http://47.107.52.7:88/member/photo/collect";

    // post：用户取消对图文分享的收藏
    public static final String COLLECT_CANCEL = "http://47.107.52.7:88/member/photo/collect/cancel";

    // get：获取一级评论； post：新增一个图片分享的一级评论
    public static final String FIRST = "http://47.107.52.7:88/member/photo/comment/first";

    // get：获取二级评论； post： 新增一个图片分享的二级评论或回复
    public static final String SECOND = "http://47.107.52.7:88/member/photo/comment/second";
}
