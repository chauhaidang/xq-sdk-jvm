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
        <View style={styles.container}>
            <Text>Welcome to OMITRIX</Text>
            <TextInput style={styles.input} placeholder={'User name'}
                       onChangeText={text => onInputUserName(text)} value={userName}/>
            <TextInput style={styles.input} placeholder={'Password'} secureTextEntry={true}
                       onChangeText={text => onInputPassword(text)} value={password}/>
            <StatusBar style="auto"/>
            <Button style={styles.container} title="Login" testID='btnLogin' onPress={() => login()}/>
            <Button style={styles.container} title="Logout" testID='btnLogout' onPress={() => logout()}/>
            <Text style={styles.text}>{val}</Text>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
        alignItems: 'center',
        justifyContent: 'center',
    },
    input: {
        margin: 20,
        borderStyle: 'dashed',
        borderColor: 'black'
    },
    text: {
        margin: 20,
        fontStyle: 'italic',
        fontWeight: "bold",
        alignSelf: "center"
    },
});
