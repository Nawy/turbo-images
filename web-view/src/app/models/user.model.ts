/**
 * Created by ermolaev on 5/9/17.
 */

export class User {

  private _id: number;
  private _name: string;
  private _avatarPath: string;


  constructor(id: number, name: string, avatarPath: string) {
    this._id = id;
    this._name = name;
    this._avatarPath = avatarPath;
  }

  get id(): number {
    return this._id;
  }

  set id(value: number) {
    this._id = value;
  }


  get name(): string {
    return this._name;
  }

  set name(value: string) {
    this._name = value;
  }

  get avatarPath(): string {
    return this._avatarPath;
  }

  set avatarPath(value: string) {
    this._avatarPath = value;
  }
}
