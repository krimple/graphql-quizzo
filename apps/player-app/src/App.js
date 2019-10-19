import './App.scss';

import React from 'react'
import MainPanel from './components/MainPanel';
import {Provider} from 'react-redux';
import buildStore from './store/build-store';

export default function App () {
  const store = buildStore();
  return (
      <Provider store={store}>
        <MainPanel />
      </Provider>
  );
}
