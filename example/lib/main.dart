import 'package:flutter/material.dart';
import 'package:linphonesdk/call_state.dart';
import 'dart:async';
import 'package:linphonesdk/linphoneSDK.dart';
import 'package:linphonesdk/login_state.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  final _linphoneSdkPlugin = LinphoneSDK();
  final _textEditingController = TextEditingController();

  @override
  void dispose() {
    _linphoneSdkPlugin.removeLoginListener();
    super.dispose();
  }

  @override
  void initState() {
    super.initState();
    requestPermissions();
  }

  Future<void> requestPermissions() async {
    try {
      _linphoneSdkPlugin.requestPermissions();
    } catch (e) {
      print("Error on request permission. ${e.toString()}");
    }
  }

  Future<void> login() async {
    await _linphoneSdkPlugin.login(
        userName: "302", domain: "192.168.1.254:3939", password: "Hi@123");
  }

  Future<void> call() async {
    if (_textEditingController.value.text.isNotEmpty) {
      String number = _textEditingController.value.text;
      await _linphoneSdkPlugin.call(number: number);
    }
  }

  Future<void> forward()async{
  await  _linphoneSdkPlugin.callTransfer(destination: "1001");
  }

  Future<void> hangUp() async {
    await _linphoneSdkPlugin.hangUp();
  }

  Future<void> toggleSpeaker()async{
    await _linphoneSdkPlugin.toggleSpeaker();
  }

  Future<bool> toggleMute()async{
   return await _linphoneSdkPlugin.toggleMute();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Linphone SDK example app'),
        ),
        body: ListView(
          padding: const EdgeInsets.all(20),
          children: [
            const SizedBox(height: 20),
            ElevatedButton(
                onPressed: () {
                  login();
                },
                child: const Text("Login")),
            const SizedBox(height: 20),
            StreamBuilder<LoginState>(
              stream: _linphoneSdkPlugin.addLoginListener(),
              builder: (context, snapshot) {
                LoginState status = snapshot.data ?? LoginState.none;
                return Text("Login status: ${status.name}");
              },
            ),
            const SizedBox(height: 20),
            TextFormField(
              controller: _textEditingController,
              keyboardType: TextInputType.phone,
              decoration: const InputDecoration(
                  icon: Icon(Icons.phone),
                  hintText: "Input number",
                  labelText: "Number"),
            ),
            const SizedBox(height: 20),
            ElevatedButton(onPressed: call, child: const Text("Call")),
            const SizedBox(height: 20),
            ElevatedButton(
                onPressed: () {
                  hangUp();
                },
                child: const Text("HangUp")),
            const SizedBox(height: 20),
            ElevatedButton(
                onPressed: () {
                  toggleSpeaker();
                },
                child: const Text("Speaker")),
            const SizedBox(height: 20),
            ElevatedButton(
                onPressed: () {
                  toggleMute();
                },
                child: const Text("Mute")),
            const SizedBox(height: 20),
            ElevatedButton(
                onPressed: () {
                  forward();
                },
                child: const Text("Forward")),
            const SizedBox(height: 20),
            StreamBuilder<CallState>(
              stream: _linphoneSdkPlugin.addCallStateListener(),
              builder: (context, snapshot) {
                CallState status = snapshot.data ?? CallState.idle;
                return Text("Call status: ${status.name.toUpperCase()}");
              },
            ),
          ],
        ),
      ),
    );
  }
}
