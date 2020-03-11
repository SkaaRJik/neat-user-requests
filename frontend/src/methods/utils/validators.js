import AuthApi from "../api/AuthAPI";

const reg = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,24}))$/

export function isEmailValid(email) {
    return (email == "") ? false : (reg.test(email))
}

export async function checkEmailExist(email) {
    try {
        const result = await AuthApi.checkEmail(email)
        console.log('[Validators].checkEmail() result:', result.data)
        return result

    } catch (e) {
        console.log('[Validators].checkEmail() error:', e.response ? e.response.data.message : e.toString())
        return false
    }
}
