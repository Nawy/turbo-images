import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { RouterModule } from '@angular/router'
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { AppComponent } from './components/app.component';
import {SignupComponent} from "./components/signup.component";
import {SigninComponent} from "./components/signin.component";
import {PostComponent} from "./components/post.component";
import {NavbarComponent} from "./components/navbar.component";
import {ImagesComponent} from "./components/images.component";
import {CommentComponent} from "./components/comment.component";
import {SettingsComponent} from "./components/settings.component";
import {PersonalPostsComponent} from "./components/personal-posts.component";
import {PostPreviewComponent} from "./components/postpreview.component";
import {AuthorizationService} from "./service/authorization.service";
import {UserService} from "./service/user.service";
import {LoggedGuard} from "./utils/logged.guard";
import {NotLoggedGuard} from "./utils/not-logged.guard";
import {UploadComponent} from "./components/uploads/upload.component";
import {ImageService} from "./service/image.service";
import {PersonalImagesComponent} from "./components/personal-images/personal-images.component";
import {ImagePreviewComponent} from "./components/image-preview.component";
import {UploadPreviewComponent} from "./components/uploads/upload-preview.component";
import {UploadModalComponent} from "./components/uploads/upload-modal.component";
import {PersonalImageModalComponent} from "./components/personal-images/personal-image-modal.component";
import {PersonalHolderService} from "./service/personal-holder.service";

@NgModule({
  declarations: [
    AppComponent,
    SignupComponent,
    SigninComponent,
    PostComponent,
    NavbarComponent,
    ImagesComponent,
    CommentComponent,
    SettingsComponent,
    PersonalPostsComponent,
    PersonalImagesComponent,
    PostPreviewComponent,
    ImagePreviewComponent,
    UploadComponent,
    UploadPreviewComponent,
    UploadComponent,
    UploadModalComponent,
    PersonalImageModalComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    NgbModule.forRoot(),
    RouterModule.forRoot([
      {
        path: "",
        component: ImagesComponent
      },
      {
        path: "signup",
        component: SignupComponent,
        canActivate: [LoggedGuard]
      },
      {
        path: "signin",
        component: SigninComponent,
        canActivate: [LoggedGuard]
      },
      {
        path: "settings",
        component: SettingsComponent,
        canActivate: [NotLoggedGuard]
      },
      {
        path: "my-posts",
        component: PersonalPostsComponent,
        canActivate: [NotLoggedGuard]
      },
      {
        path: "my-images",
        component: PersonalImagesComponent,
        canActivate: [NotLoggedGuard]
      },
      {
        path: 'post/:id',
        component: PostComponent
      },
      {
        path: 'uploads',
        component: UploadComponent
      }
    ])
  ],
  providers: [
    AuthorizationService,
    UserService,
    ImageService,
    PersonalHolderService,
    LoggedGuard,
    NotLoggedGuard
  ],
  bootstrap: [AppComponent],
  entryComponents: [
    UploadModalComponent,
    PersonalImageModalComponent
  ]
})
export class AppModule { }
