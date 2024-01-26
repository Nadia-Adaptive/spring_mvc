export interface LoginRequest {
    username: string
    password: string
}

export enum SignInState {
    SIGNED_OUT = 'SIGNED_OUT',
    SIGN_ERROR = 'SIGN_ERROR',
    SIGNING_IN = 'SIGNING_IN',
}

export interface User extends Omit<UserDetails, 'password'> {
    id: number
    admin: boolean
    token: string
    blocked?: boolean
}

export interface UserDetails {
    username: string
    password: string
    firstName: string
    lastName: string
    organisation: string
}
