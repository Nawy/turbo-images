export const environment = {
  production: true,
  host: "http://localhost:8030/api",
  requests: {
    signinUrl: "/signin",
    signupUrl: "/signup",
    logoutUrl: "/logout",
    getUserInfoUrl: "/get/user/info",
    isUserExistsUrl: "/is/user/exists"
  },
  tokenName: "SESSIONID"
};
