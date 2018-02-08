import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { RouterModule } from '@angular/router'
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { AppComponent } from './components/app.component';
import {SignupComponent} from "./components/signup.component";
import {SigninComponent} from "./components/signin.component";
import {PostComponent} from "./components/posts/post.component";
import {NavbarComponent} from "./components/navbar.component";
import {AllPostsComponent} from "./components/posts/all-posts.component";
import {CommentComponent} from "./components/comments/comment.component";
import {CommentsBlockComponent} from "./components/comments/comments-block.component";
import {SettingsComponent} from "./components/settings.component";
import {PersonalPostsComponent} from "./components/posts/personal-posts.component";
import {PostPreviewComponent} from "./components/posts/post-preview.component";
import {AuthorizationService} from "./service/authorization.service";
import {UserService} from "./service/user.service";
import {LoggedGuard} from "./utils/logged.guard";
import {NotLoggedGuard} from "./utils/not-logged.guard";
import {UploadComponent} from "./components/uploads/upload.component";
import {ImageService} from "./service/image.service";
import {PersonalImagesComponent} from "./components/images/personal-images.component";
import {ImagePreviewComponent} from "./components/images/image-preview.component";
import {UploadPreviewComponent} from "./components/uploads/upload-preview.component";
import {UploadModalComponent} from "./components/uploads/upload-modal.component";
import {PersonalImageModalComponent} from "./components/images/personal-image-modal.component";
import {PostRatingComponent} from "./components/posts/post-rating.component";
import {PersonalHolderService} from "./service/personal-holder.service";
import {ImagePageComponent} from "./components/images/image-page.component";
import {SessionService} from "./service/session.service";
import {PostService} from "./service/post.service";
import {PostSearchComponent} from "./components/search/post-search.component";
import {CommentService} from "./service/comment.service";
import {TextInputComponent} from "./components/custom/text-input.component";

@NgModule({
  declarations: [
    AppComponent,
    SignupComponent,
    SigninComponent,
    PostComponent,
    NavbarComponent,
    AllPostsComponent,
    CommentComponent,
    CommentsBlockComponent,
    SettingsComponent,
    PersonalPostsComponent,
    PersonalImagesComponent,
    PostPreviewComponent,
    ImagePreviewComponent,
    UploadComponent,
    UploadPreviewComponent,
    UploadComponent,
    UploadModalComponent,
    PersonalImageModalComponent,
    ImagePageComponent,
    PostRatingComponent,
    PostSearchComponent,
    TextInputComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    NgbModule.forRoot(),
    RouterModule.forRoot([
      {
        path: "",
        component: AllPostsComponent
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
        path: 'img/:id',
        component: ImagePageComponent
      },
      {
        path: 'search',
        component: PostSearchComponent
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
    NotLoggedGuard,
    SessionService,
    PostService,
    CommentService
  ],
  bootstrap: [AppComponent],
  entryComponents: [
    UploadModalComponent,
    PersonalImageModalComponent
  ]
})
export class AppModule { }
