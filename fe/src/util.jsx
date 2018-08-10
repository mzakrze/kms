export function beforeSendRequest(xmlHttpRequest){
    setJwtToken(xmlHttpRequest);
}

const setJwtToken = (xmlHttpRequest: any) => {
    let jwt = localStorage.getItem('JwtAuthentication');
    xmlHttpRequest.setRequestHeader('Authentication', jwt);
}