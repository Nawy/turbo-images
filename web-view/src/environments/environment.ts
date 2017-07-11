// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --env=prod` then `environment.prod.ts` will be used instead.
// The list of which env maps to which file can be found in `.angular-cli.json`.

export const environment = {
  production: false,
  host: "http://localhost:8030/api",
  imageHost: "http://192.168.200.1", // should be port 80
  requests: {
    signinUrl: "/signin",
    signupUrl: "/signup",
    logoutUrl: "/logout",
    getUserInfoUrl: "/get/user/info",
    isUserExistsUrl: "/is/user/exists",
    addUserImageUrl: "/add/user/image"
  },
  tokenName: "SESSIONID"
};
