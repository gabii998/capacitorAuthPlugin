import { WebPlugin } from '@capacitor/core';

import type { BiometricPlugin } from './definitions';

export class BiometricWeb extends WebPlugin implements BiometricPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
