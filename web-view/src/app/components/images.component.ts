/**
 * Created by ermolaev on 5/9/17.
 */

import {Component} from '@angular/core';
import {PostPreview} from '../models/postpreview.model';

@Component({
  selector: "s-images",
  templateUrl: './../templates/images.template.html',
  styleUrls: ['./../css/images.style.css']
})
export class ImagesComponent {

  postPreviews: PostPreview[];

  constructor() {
    const count: number = 100;
    this.postPreviews = new Array<PostPreview>(count);
    var pathes: string[] = ["https://i.imgur.com/uuh9RZ9b.jpg", "https://i.imgur.com/djKgENAb.jpg"];

    for(var i = 0; i < count;) {
      var path:string;
      for(path in pathes) {
        this.postPreviews[i] = new PostPreview(i, pathes[path], Math.floor(Math.random()*100));
        i++;
        if(i >= count) break;
      }
    }
  }
}

