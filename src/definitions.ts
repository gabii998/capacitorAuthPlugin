export interface BiometricPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
  deviceSupportBiometricLogin():Promise<{value:boolean}>;
}
