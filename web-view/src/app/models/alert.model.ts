/**
 * Created by ermolaev on 6/27/17.
 */

export enum AlertType {
  WARNING,
  INFO,
  SUCCESS,
  DANGER
}

export class Alert {
  type : string;
  message : string;

  constructor(_type : AlertType, _message : string) {
    switch (_type) {
      case AlertType.DANGER: {
        this.type = "danger";
        break;
      }
      case AlertType.INFO: {
        this.type = "info";
        break;
      }
      case AlertType.SUCCESS: {
        this.type = "success";
        break;
      }
      case AlertType.WARNING: {
        this.type = "warning";
        break;
      }
    }
    this.message = _message;
  }
}
