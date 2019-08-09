import Fingerprint2 from 'fingerprintjs2'





export default {
    makeFinger: () => {return new Fingerprint2().get(function(result, components) {
        console.log(result) // a hash, representing your device fingerprint
        console.log(components) // an array of FP components
    })
        /*if (window.requestIdleCallback) {
            requestIdleCallback(function () {
                new Fingerprint2().get(function(result, components) {
                    console.log(result) // a hash, representing your device fingerprint
                    console.log(components) // an array of FP components
                })
            })
        } else {
            setTimeout(function () {
                new Fingerprint2().get(function(result, components) {

                })
            }, 500)
        }},*/


}

