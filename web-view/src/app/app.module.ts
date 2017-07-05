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
import {MaterialsComponent} from "./components/materials.component";
import {PostPreviewComponent} from "./components/postpreview.component";
import {AuthorizationService} from "./service/authorization.service";
import {UserService} from "./service/user.service";

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
    MaterialsComponent,
    PostPreviewComponent
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
        component: SignupComponent
      },
      {
        path: "signin",
        component: SigninComponent
      },
      {
        path: "settings",
        component: SettingsComponent
      },
      {
        path: "materials",
        component: MaterialsComponent
      },
      {
        path: 'post/:id',
        component: PostComponent
      }
    ])
  ],
  providers: [
    AuthorizationService,
    UserService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
