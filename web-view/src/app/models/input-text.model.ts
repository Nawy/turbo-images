/**
 * Created by ermolaev on 7/3/17.
 */

export const VALID_INPUT : InputTextFormClass = {
  groupClass: "has-success",
  formControlClass: "is-valid"
};

export const INVALID_INPUT : InputTextFormClass = {
  groupClass: "has-danger",
  formControlClass: "is-invalid"
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
