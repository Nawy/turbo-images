/**
 * Created by ermolaev on 5/9/17.
 */

enum ClientType {
  IPHONE,
  ANDROID,
  WINDOWS_PHONE,
  PC,
  MAC,
  LINUX
}

export class Comment {
  private _authorId: number;
  private _authorDevice: ClientType;
  private _postTime: string;
  private _content: string;
  private _rating: number;


  constructor(
    authorId: number,
    authorDevice: ClientType,
    postTime: string,
    content: string,
    rating: number
  ) {
    this._authorId = authorId;
    this._authorDevice = authorDevice;
    this._postTime = postTime;
    this._content = content;
    this._rating = rating;
  }


  get authorId(): number {
    return this._authorId;
  }

  set authorId(value: number) {
    this._authorId = value;
  }

  get authorDevice(): ClientType {
    return this._authorDevice;
  }

  set authorDevice(value: ClientType) {
    this._authorDevice = value;
  }

  get postTime(): string {
    return this._postTime;
  }

  set postTime(value: string) {
    this._postTime = value;
  }

  get content(): string {
    return this._content;
  }

  set content(value: string) {
    this._content = value;
  }

  get rating(): number {
    return this._rating;
  }

  set rating(value: number) {
    this._rating = value;
  }
}
