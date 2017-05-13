/**
 * Created by ermolaev on 5/9/17.
 */

import {Component} from '@angular/core';
import {Image} from '../models/image.model';

@Component({
  selector: "s-images",
  templateUrl: './../templates/images.template.html',
  styleUrls: ['./../css/images.style.css']
})
export class ImagesComponent {

  images: Image[];

  constructor() {
    const count: number = 100;
    this.images = new Array<Image>(count);
    var pathes: string[] = ["https://i.imgur.com/uuh9RZ9b.jpg", "https://i.imgur.com/djKgENAb.jpg"];

    for(var i = 0; i < count;) {
      var path:string;
      for(path in pathes) {
        this.images[i] = new Image("1", pathes[path], i);
        i++;
        if(i >= count) break;
      }
    }
  }
}

