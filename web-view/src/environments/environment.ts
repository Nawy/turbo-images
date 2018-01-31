// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --env=prod` then `environment.prod.ts` will be used instead.
// The list of which env maps to which file can be found in `.angular-cli.json`.

export const environment = {
  production: false,
  host: "http://localhost:8030/api",
  imageHost: "192.168.1.64", // should be port 80
  clientType: "BROWSER",
  requests: {

    // authorization
    signinUrl: "/signin",
    signupUrl: "/signup",
    logoutUrl: "/logout",

    // user service
    getUserInfoUrl: "/get/user/info",
    isUserExistsUrl: "/is/user/exists",
    updateUserEmail: "/update/user/email",
    updateUserName: "/update/user/name",
    updateUserPassword: "/update/user/password",

    // image service
    addUserImageUrl: "/add/user/image",
    getUserImageUrl: "/get/user/images",
    editUserImageDescription: "/edit/user/image/description",
    editUserImageName: "/edit/user/image/name",
    getUserImageByIdUrl: "/get/user/image/",

    //posts
    savePost: "/save/post",
    getUserPostsByDate: "/get/user/posts/by_date",
    getPostsByDate:"/get/posts/by_date",
    getPosts: "/get/viral/post",
    getPost: "/get/post/",
    editPostName: "/edit/post/name",
    editPostDescription: "/edit/post/description",
    addPostTag: "/add/post/tag",
    removePostTag: "/remove/post/tag",
    increaseViews: "/post/views",
    changeRating: "/post/rating",
    findPost: "/post"
  },
  tokenName: "SESSIONID",
  uploadPersonImage: {
    pageSize: 30
  },
  pageSize:30,
  autoUpdateInterval: 2000 //autoupdate in milliseconds
};
