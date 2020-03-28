import { createAppContainer } from 'react-navigation';
import { createStackNavigator } from 'react-navigation-stack';

import BookingsScreen from './src/screens/BookingsScreen';

const navigator = createStackNavigator({
  Bookings: BookingsScreen
}, {
  initialRouteName: 'Bookings',
  defaultNavigationOptions: {
    title: 'My Bookings'
  }
});

export default createAppContainer(navigator);
