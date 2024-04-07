const BASE_URL = `${import.meta.env.VITE_AUTH_URL}`;
import {Buffer} from "buffer";

export const authClient = {
    getToken: async(url: string, data: URLSearchParams) => {
        const params = "code=" + data.get("code") + "&redirect_uri=" + data.get("redirect_uri") + "&code_verifier=" + data.get("code_verifier") + "&grant_type=" + data.get("grant_type") + "&client_id=" + data.get("client_id")
        const response = await fetch(`${BASE_URL}/${url}?${params}`, {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-type": "application/x-www-form-urlencoded",
                "Authorization": `Basic ${Buffer.from("client:secret").toString("base64")}`,
            }
        });
        if (!response.ok) {
            throw new Error("Failed loading data");
        }
        return response.json();
    },
    logout: async() => {
        const response = await fetch(`${BASE_URL}/logout`, {
            method: "GET",
            credentials: "include",
            headers: {
                "Content-type": "application/json",
                "Authorization": `Bearer ${localStorage.getItem("id_token")}`,
            }
        });
        if (!response.ok) {
            throw new Error("Failed logout");
        }
    }
}