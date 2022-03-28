const Koa = require('koa');
const app = new Koa();
const server = require('http').createServer(app.callback());
const WebSocket = require('ws');
const wss = new WebSocket.Server({ server });
const Router = require('koa-router');
const cors = require('koa-cors');
const bodyparser = require('koa-bodyparser');
const { INSPECT_MAX_BYTES } = require('buffer');

app.use(bodyparser());
app.use(cors());
app.use(async (ctx, next) => {
    const start = new Date();
    await next();
    const ms = new Date() - start;
    console.log(`${ctx.method} ${ctx.url} ${ctx.response.status} - ${ms}ms`);
});
  
app.use(async (ctx, next) => {
    await new Promise(resolve => setTimeout(resolve, 2000));
    await next();
});
  
app.use(async (ctx, next) => {
    try {
      await next();
    } catch (err) {
      ctx.response.body = { issue: [{ error: err.message || 'Unexpected error' }] };
      ctx.response.status = 500;
    }
});

class ToDoItem{
    constructor({id, title, description, status, deadline, createdAt}) {
        this.id=id;
        this.title=title;
        this.description=description;
        this.status=status;
        this.deadline=deadline;
        this.createdAt=createdAt;
    }
}

const todos=[];

for(let i=0; i<3; i++){
    todos.push(new ToDoItem(
        {
            id: `${i}`,
            title: `item ${i}`,
            description: `desc ${i}`,
            status: `Not started`,
            deadline: new Date(Date.now()+i),
            createdAt:new Date(Date.now())
        }))
}

let lastUpdated = todos[todos.length - 1].createdAt;
let lastId = todos[todos.length - 1].id;

const broadcast = data => wss.clients
    .forEach(client =>{
        if(client.readyState === WebSocket.OPEN){
            client.send(JSON.stringify(data));
        }
    });

const router = new Router();

router.get('/item', ctx =>{
    const ifModifiedSince = ctx.request.get('If-Modified-Since');
    if(ifModifiedSince && new Date(ifModifiedSince).getTime()>=lastUpdated.getTime() - lastUpdated.getMilliseconds()){
        ctx.response.status = 304; // NOT MODIFIED
        return;
    }
    else {
        ctx.response.set('Last-Modified', lastUpdated);
        ctx.response.body = todos;
        ctx.response.status = 200;
      }
    
});

router.get('item/:id', async (ctx) => {
    const todoId = ctx.request.params.id;
    const todo= todos.find(item => todoId === item.id );
    if(todo){
        ctx.request.body = todo;
        ctx.response.status=200; //ok
    }
    else{
        ctx.response.body = {
            issue: [{ warning: `item with id ${todoId} not found` }] 
        };
        ctx.response.status = 404; // NOT FOUND 
    }
});


const createToDo = async (ctx) => {
    const toDo = ctx.request.body;
    console.log(toDo.title);
    if (!toDo.title) {// validation
        ctx.response.body = {
            issue: [{ error: 'Title is missing' }]
        };
        ctx.response.status = 400; //  BAD REQUEST
        return;

    }
    toDo.id = `${parseInt(lastId) + 1}`;
    lastId = toDo.id;
    toDo.createdAt = new Date;
    todos.push(toDo);
    ctx.response.body=toDo;
    ctx.response.status = 201; // CREATED
    broadcast({ event: 'created', payload: { toDo } });
    
}

router.post('/item', async (ctx) => {
    await createToDo(ctx);
});

router.put('/item/:id', async (ctx) => {
    const id = ctx.params.id;
    const toDo= ctx.request.body;
    toDo.createdAt = new Date();
    const todoId= toDo.id;
    if(todoId && id !== toDo.id){
        ctx.response.body = { issue: [{ error: `Param id and body id should be the same` }] };
        ctx.response.status = 400; // BAD REQUEST
        return;
    }
    if(!todoId){
        await createToDo(ctx);
        return;
    }
    const index = todos.findIndex(item => item.id === id);
    if(index === -1){
        ctx.response.body = { issue: [{ error: `todo with id ${id} not found` }] };
        ctx.response.status = 400; // BAD REQUEST
        return;
    }

    todos[index] = toDo;
    lastUpdated = new Date();
    ctx.response.body = toDo;
    ctx.response.status = 200; // OK
    broadcast({ event: 'updated', payload: {toDo } });


});

router.del('/item/:id', ctx=>{
    console.log(1);
    const id = ctx.params.id;
    console.log(1);
    const index = todos.findIndex(item => id === item.id);
    console.log(1);
    if(index !== -1){
        console.log(1);
        const toDo = todos[index];
        todos.splice(index,1);
        lastUpdated = new Date();
        broadcast({ event: 'deleted', payload: { toDo } });
        ctx.response.body = toDo;
    }
    ctx.response.status = 204; // no content

});


app.use(router.routes());
app.use(router.allowedMethods());


app.listen(3000);
console.log("Aplication is running");
console.log("application is running on port 3000");