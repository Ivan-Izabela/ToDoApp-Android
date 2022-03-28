import 'package:flutter/material.dart';
import 'package:todoapp_non_nativ/domain/ToDoItem.dart';
import 'package:todoapp_non_nativ/screens/todoEdit.dart';

class ToDoWidget extends StatefulWidget{
  ToDoWidget(this.item);
  final ToDoItem item;

  @override
  _ToDoWidgetState createState()=>_ToDoWidgetState();


}

class _ToDoWidgetState  extends State<ToDoWidget>{
  _ToDoWidgetState();
  @override
  Widget build(BuildContext context) {
    return ListTile(
      title: Center(child: Text(widget.item.title)),
      subtitle: Column(
        children: <Widget>[
          Text("Status: "+ widget.item.state + " | Deadline: "+ widget.item.deadline),
          ElevatedButton(
            child: Text('Edit'),
            onPressed: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (context) => TodoEdit(widget.item)),
              );
            },
          ),

        ],
      ),



    );
  }


}