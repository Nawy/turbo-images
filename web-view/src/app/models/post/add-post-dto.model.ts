import {UserImage} from "../user-image.model";
import {Post} from "./post.model";
import {DeviceType} from "../device-type.model";

export class TransferPost {

  id: string;
  name: string;
  description: string;
  image_ids: Array<string>;
  device_type: DeviceType;
  tags: Array<string>;
  visible: boolean = true;


  constructor(id: string,
              name: string,
              description: string,
              images: Array<UserImage>,
              deviceType: DeviceType,
              tags: Array<string>) {

    this.id = id;
    this.name = name;
    this.description = description;
    this.device_type = deviceType;
    this.tags = tags;
    this.visible = true;
    this.setImages(images)
  }

  public static makeTransferPost(post: Post): TransferPost {
    const transferPost: TransferPost = new TransferPost(
      post.id,
      post.name,
      post.description,
      post.images,
      post.device_type,
      post.tags
    );
    transferPost.visible = post.visible;
    return transferPost;
  }

  public static emptyTransferPost() {
    return new TransferPost(
      null,
      null,
      null,
      null,
      DeviceType.PC,
      null
    );
  }

  public setImages(images:Array<UserImage>){
    if(images != null) {
      this.image_ids = images.map(image => image.id);
    }
  }

}
