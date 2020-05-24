import store from '../../store/vue-store'

export function getShortNameForAvatar(){
    const userInfo = store.getters['auth/userData']
    return userInfo.firstname[0] + userInfo.lastname[0];
}


