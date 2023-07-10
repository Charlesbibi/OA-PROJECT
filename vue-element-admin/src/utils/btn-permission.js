import store from '@/store'

/**
 * 判断当前用户是否有此按钮权限
 * 按钮权限字符串 permission 
 */
export default function hasBtnPermission(permission) {
  // 得到当前用户的所有按钮权限
  const myBtns = store.getters.buttons
  console.log("my buttons>>>>>>>>>>>>",myBtns)
  // 如果指定的功能权限在myBtns中, 返回true ==> 这个按钮就会显示, 否则隐藏
  const key = myBtns.indexOf(permission) !== -1
  console.log("my buttons>>>>>>>>>>>>",key)
  return key
}