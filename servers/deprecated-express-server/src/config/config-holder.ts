// TODO - remove this?
import {PubSub} from 'apollo-server';
export class ConfigHolder {
  private _pubsub: PubSub | undefined;

  get pubsub(): PubSub | undefined {
   return this._pubsub;
  }

  set pubsub(pubsub: PubSub | undefined) {
    if (!pubsub) {
      throw new Error('No pubsub passed');
    }
    this._pubsub = pubsub;
  }
}

// this is a singleton (node app) so we hold on to the instance here and export it
const config: ConfigHolder = new ConfigHolder();
export default config;
