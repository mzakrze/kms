export function fetchMyTasks(){
    return $.ajax({
        url: '/api/v1/task',
        type: "GET",
        beforeSend: preAuthenticatedRequest,
        async: true,
        headers: {"Content-Type": "application/json"},
    }).done(postResponse);
}

export function createNewTask(title, description){
    return $.ajax({
        url: '/api/v1/task',
        type: "POST",
        beforeSend: preAuthenticatedRequest,
        data: JSON.stringify({title, description}),
        async: true,
        headers: {"Content-Type": "application/json"},
    }).done(postResponse);
}

export function deleteTask(id){
    return $.ajax({
        url: '/api/v1/task/' + id,
        type: "DELETE",
        beforeSend: preAuthenticatedRequest,
        async: true,
        headers: {"Content-Type": "text/html"},
    }).done(postResponse);
}

// FIXME - wynieść poniższe do jakiegoś utila

const preAuthenticatedRequest = (xmlHttpRequest) => {
    setJwtToken(xmlHttpRequest);
    addVersioningHeader(xmlHttpRequest);

}

const postResponse = (xmlHttpResonse) => {

}

export function beforeSend(xmlHttpRequest){
    setJwtToken(xmlHttpRequest);
}

const setJwtToken = (xmlHttpRequest: any) => {
    let jwt = localStorage.getItem('JwtAuthentication');
    xmlHttpRequest.setRequestHeader('Authentication', jwt);
}

const addVersioningHeader = (xmlHttpRequest: any) => {

}