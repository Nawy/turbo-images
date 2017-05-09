/**
 * Created by ermolaev on 5/9/17.
 */

export class User {

  private _id: number;
  private _nickname: string;
  private _avatarPath: string;


  constructor(id: number, nickname: string, avatarPath: string) {
    this._id = id;
    this._nickname = nickname;
    this._avatarPath = avatarPath;
  }

  get id(): number {
    return this._id;
  }

  set id(value: number) {
    this._id = value;
  }

  get nickname(): string {
    return this._nickname;
  }

  set nickname(value: string) {
    this._nickname = value;
  }

  get avatarPath(): string {
    return this._avatarPath;
  }

  set avatarPath(value: string) {
    this._avatarPath = value;
  }
}
