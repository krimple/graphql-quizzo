export default function(root, args, {currentUser}) {
  // the current user is stored in context from the context function for the auth schema module
  return currentUser;
}
