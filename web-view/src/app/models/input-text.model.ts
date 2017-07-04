/**
 * Created by ermolaev on 7/3/17.
 */

export const SUCCESS_INPUT : InputTextFormClass = {
  groupClass: "has-success",
  formControlClass: "form-control-success"
};

export const WARNING_INPUT : InputTextFormClass = {
  groupClass: "has-warning",
  formControlClass: "form-control-warning"
};

export const DANGER_INPUT : InputTextFormClass = {
  groupClass: "has-danger",
  formControlClass: "form-control-danger"
};

export const EMPTY_INPUT : InputTextFormClass = {
  groupClass: "",
  formControlClass: ""
};


export class InputTextFormClass {
  groupClass: string;
  formControlClass: string;

  constructor(groupClass : string, formControlClass : string) {
    this.groupClass = groupClass;
    this.formControlClass = formControlClass;
  }
}

export class InputTextForm extends InputTextFormClass {
  value : string;

  constructor(inputForm : InputTextFormClass, value : string) {
    super(inputForm.groupClass, inputForm.formControlClass);
    this.value = value;
  }

  public setValue(inputForm : InputTextFormClass, value : string) {
    this.groupClass = inputForm.groupClass;
    this.formControlClass = inputForm.formControlClass;
    this.value = value;
  }
}
