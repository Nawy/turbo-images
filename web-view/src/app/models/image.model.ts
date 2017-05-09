/**
 * Created by ermolaev on 5/9/17.
 */

export class Image {
  private _hash: string;
  private _path: string;


  constructor(hash: string, path: string) {
    this._hash = hash;
    this._path = path;
  }

  get hash(): string {
    return this._hash;
  }

  set hash(value: string) {
    this._hash = value;
  }

  get path(): string {
    return this._path;
  }

  set path(value: string) {
    this._path = value;
  }
}
