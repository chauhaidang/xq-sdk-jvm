import {useState} from 'react';
import {StatusBar} from 'expo-status-bar';
import {Button, Modal, StyleSheet, Text, TextInput, View} from 'react-native';
import {SHA256} from 'crypto-js';

export default function App() {
    const [val, setVal] = useState('')
    const [userName, setUserName] = useState('')
    const [password, setPassword] = useState('')

    function generateRandomString(length) {
        let randomChars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
        let result = '';
        for (let i = 0; i < length; i++) {
            result += randomChars.charAt(Math.floor(Math.random() * randomChars.length));
        }
        return result;
    }

    const login = () => {
        let output = 'Please enter username and password!'
        if (userName !== '' && password !== '') {
            output = `Hey ${userName.toString()}:${SHA256()}`
        }
        setVal(output)
    }

    const logout = () => {
        setVal('')
        setUserName('')
        setPassword('')
    }

    function onInputUserName(value) {
        setUserName(value)
    }

    function onInputPassword(value) {
        setPassword(value)
    }

    return (
        <View style={styles.appContainer}>
            <View style={styles.inputContainer}>
                <TextInput style={styles.textInput} placeholder={'Your course goal'}/>
                <Button style={styles.button} title={'Add goal'}/>
            </View>
            <View>
                <Text>List of goals...</Text>
            </View>
        </View>
    );
}

const styles = StyleSheet.create({
    appContainer: {
        padding: 50
    },
    inputContainer: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center'
    },
    textInput: {
        borderColor: '#a2d5c6',
        borderWidth: 1,
        width: '80%',
        marginRight: 8,
        padding: 8
    },
    button: {
    }
});
